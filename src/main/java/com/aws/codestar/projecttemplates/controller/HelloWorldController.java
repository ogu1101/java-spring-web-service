package com.aws.codestar.projecttemplates.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/")
public class HelloWorldController {

  /** response.Outputのフォーマット. */
  private static final String MESSAGE_FORMAT = "Hello %s!";

  /**
   * helloWorldのGetメソッド.
   *
   * @param name
   *          名前
   * @return responseEntity
   */
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<String> helloWorldGet(
      @RequestParam(value = "name", defaultValue = "World") final String name) {
    return ResponseEntity.ok(createResponse(name));
  }

  /**
   * helloWorldのPOSTメソッド.
   *
   * @param name
   *          名前
   * @return responseEntity
   */
  @RequestMapping(method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<String> helloWorldPost(
      @RequestParam(value = "name", defaultValue = "World") final String name) {
    return ResponseEntity.ok(createResponse(name));
  }

  /**
   * responseを作成する.
   *
   * @param name
   *          名前
   * @return response
   */
  private String createResponse(final String name) {
    return new JSONObject()
        .put("Output", String.format(MESSAGE_FORMAT, name))
        .toString();
  }
}
