package com.niuml;

/***
 * User:niumengliang
 * Date:2024/12/18
 * Time:17:41
 */
public interface LocalCommandExecutor {

    ExecuteResult executeCommand(String command, long timeout);
}
