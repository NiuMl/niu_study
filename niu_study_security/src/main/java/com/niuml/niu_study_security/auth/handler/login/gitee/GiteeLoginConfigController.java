package com.niuml.niu_study_security.auth.handler.login.gitee;

import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/public/login/gitee")
public class GiteeLoginConfigController {

  @Value("${login.gitee.clientId}")
  private String giteeClientId;

  @Value("${login.gitee.redirectUri}")
  private String giteeCallbackEndpoint;

  @GetMapping("/config")
  public Result getA() {
    HashMap<String, Object> config = new HashMap<>();
    config.put("clientId", giteeClientId);
    config.put("redirectUri", giteeCallbackEndpoint);
    return ResultBuilder.aResult()
        .code(Result.SUCCESS_CODE)
        .data(config)
        .msg("SUCCESS")
        .build();
  }

}
