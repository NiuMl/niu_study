package com.niuml.niu_study_sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;

/***
 * @author niumengliang
 * Date:2024/12/7
 * Time:13:32
 */
public class SseEmitterLocal extends SseEmitter implements Serializable {
    public SseEmitterLocal(long l) {
        super(l);
    }
}
