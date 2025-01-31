package com.niuml;

/***
 * @author niumengliang
 * Date:2024/12/18
 * Time:17:44
 */
/**
 * LocalCommandExecutorImpl.java
 */

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LocalCommandExecutorImpl implements LocalCommandExecutor {



    static ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

    @Override
    public ExecuteResult executeCommand(String command, long timeout) {

        Process process = null;
        InputStream pIn = null;
        InputStream pErr = null;
        StreamGobbler outputGobbler = null;
        StreamGobbler errorGobbler = null;
        Future<Integer> executeFuture = null;
        try {

            System.out.println(command.toString());
            process = Runtime.getRuntime().exec(command);
            final Process p = process;

            // close process's output stream.
            p.getOutputStream().close();

            pIn = process.getInputStream();
            outputGobbler = new StreamGobbler(pIn, "OUTPUT");
            outputGobbler.start();

            pErr = process.getErrorStream();
            errorGobbler = new StreamGobbler(pErr, "ERROR");
            errorGobbler.start();

            // create a Callable for the command's Process which can be called by an Executor
            Callable<Integer> call = new Callable<Integer>() {

                public Integer call() throws Exception {

                    p.waitFor();
                    return p.exitValue();
                }
            };

            // submit the command's call and get the result from a
            executeFuture = pool.submit(call);
            int exitCode = executeFuture.get(timeout, TimeUnit.MILLISECONDS);
            return new ExecuteResult(exitCode, outputGobbler.getContent());

        } catch (IOException ex) {
            ex.printStackTrace();
            return new ExecuteResult(-1, null);
        } catch (TimeoutException ex) {
            ex.printStackTrace();
            return new ExecuteResult(-1, null);
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            return new ExecuteResult(-1, null);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return new ExecuteResult(-1, null);
        } finally {

            if (executeFuture != null) {

                try {

                    executeFuture.cancel(true);
                } catch (Exception ignore) {

                    ignore.printStackTrace();
                }
            }
            if (pIn != null) {

                this.closeQuietly(pIn);
                if (outputGobbler != null && !outputGobbler.isInterrupted()) {

                    outputGobbler.interrupt();
                }
            }
            if (pErr != null) {

                this.closeQuietly(pErr);
                if (errorGobbler != null && !errorGobbler.isInterrupted()) {

                    errorGobbler.interrupt();
                }
            }
            if (process != null) {

                process.destroy();
            }
        }
    }

    private void closeQuietly(Closeable c) {

        try {

            if (c != null) {

                c.close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
