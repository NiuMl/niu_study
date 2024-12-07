package com.niuml.niu_study_rocketmq.conf;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/***
 * @author niumengliang
 * Date:2024/12/2
 * Time:14:33
 * 这类的作用是
 *  Springboot-3.0已经放弃了spring.plants自动装配，它被/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports所取代，添加这个文件是为了兼容。
 * 所以，在resources下创建META-INF，然后在META-INF下创建文件：org.springframework.boot.autoconfigure.AutoConfiguration.imports
 * 在org.springframework.boot.autoconfigure.AutoConfiguration.imports文件中加入内容：
 * org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration
 *
 * 也可以使用下面的注解方式
 */
@Configuration
@Import({RocketMQAutoConfiguration.class})
public class RocketMQConfig {
}
