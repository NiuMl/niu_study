//package com.niuml.niu_study_sse;
//
//import jakarta.annotation.Resource;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
///***
// * @author niumengliang
// * Date:2024/12/7
// * Time:13:23
// */
//////////////////////////////这种方式不可用//////////////////////////////
//////////////////////////////原本计划把连接放入redis  没想到不行//////////////////////////////
//@Log4j2
//@RestController
//@RequestMapping("/sse")
//@CrossOrigin
//public class SseRedisController {
//
//    @Resource
//    private RedisTemplate<String,Object> redisTemplate;
//
//
//    @GetMapping("/create-connect")
//    public SseEmitterLocal createConnect(@RequestParam("userId") String userId) {
//        try {
//            // 设置超时时间，0表示不过期。默认30秒
//            SseEmitterLocal sseEmitter = new SseEmitterLocal(0L);
//            // 注册回调
//            sseEmitter.onCompletion(() -> removeSseConnection(userId, "SSE连接已关闭"));
//            sseEmitter.onError(throwable -> removeSseConnection(userId, "SSE连接出现错误"));
//            sseEmitter.onTimeout(() -> removeSseConnection(userId, "SSE连接超时"));
//            redisTemplate.opsForValue().set(userId.toString(), sseEmitter);
//            log.info("创建了用户[{}]的SSE连接", userId);
//            return sseEmitter;
//        } catch (Exception e) {
//            log.error("创建新的SSE连接异常，当前用户：" + userId, e);
//            return null;
//        }
//    }
//    /**
//     * 发送消息
//     */
//    @GetMapping("/send-message")
//    public void sendMessage(@RequestParam("userId") String userId, @RequestParam("message") String message) {
//
//        SseEmitterLocal sseEmitter = (SseEmitterLocal) redisTemplate.opsForValue().get(userId);
//        if (sseEmitter != null) {
//            try {
//                sseEmitter.send(SseEmitter.event()
//                        .name("message")
//                        .data(message)
//                        .reconnectTime(5000));
//                log.info("给用户[{}]发送消息成功: {}", userId, message);
//            } catch (Exception e) {
//                log.error("给用户[{}]发送消息失败: {}", userId, e.getMessage(), e);
//                // 如果发送失败，尝试从map中移除失效的SseEmitter
//                removeSseConnection(userId, "发送消息失败");
//            }
//        } else {
//            log.info("用户[{}]的SSE连接不存在或已关闭，无法发送消息", userId);
//        }
//    }
//
//    private void removeSseConnection(String userId, String reason) {
//        System.out.println("用户id:"+userId+" 移除");
//        SseEmitter sseEmitter = (SseEmitter) redisTemplate.opsForValue().get(userId);
//        if (sseEmitter != null) {
//            sseEmitter.complete();
//            redisTemplate.delete(userId);
//            log.info("用户[{}]的SSE连接已移除，原因：{}", userId, reason);
//        }
//    }
//}
