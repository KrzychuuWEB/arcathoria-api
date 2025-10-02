package com.arcathoria;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.GlobalController.class)
@Import({GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageSource messageSource;

    @Test
    void should_return_violations_when_request_body_is_not_valid() throws Exception {
        String invalidBody = """
                {"name": "a"}
                """;

        mockMvc.perform(post("/test-validation-body")
                        .contentType("application/json")
                        .content(invalidBody)
                        .header("Accept-Language", "en"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:validation-error"))
                .andExpect(jsonPath("$.title").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.detail").value("Request validation failed."))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_VALIDATION"))
                .andExpect(jsonPath("$.violations[0].field").value("name"))
                .andExpect(jsonPath("$.violations[0].code").value("Size"))
                .andExpect(jsonPath("$.violations[0].message").value("size must be between 2 and 5"))
                .andExpect(jsonPath("$.instance").value("/test-validation-body"));
    }

    @Test
    void should_return_violations_when_request_param_is_not_valid() throws Exception {
        mockMvc.perform(get("/test-validation-param/" + 123456).header("Accept-Language", "en"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:validation-error"))
                .andExpect(jsonPath("$.title").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.detail").value("Request validation failed."))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_VALIDATION"))
                .andExpect(jsonPath("$.violations[0].field").value("validationParamHandler.id"))
                .andExpect(jsonPath("$.violations[0].code").value("Max"))
                .andExpect(jsonPath("$.violations[0].message").value("must be less than or equal to 5"))
                .andExpect(jsonPath("$.instance").value("/test-validation-param/" + 123456));
    }

    @Test
    void should_return_bad_request_for_malformed_json() throws Exception {
        String malformed = "{ not-a-json }";

        mockMvc.perform(post("/test-validation-body")
                        .contentType("application/json")
                        .content(malformed)
                        .header("Accept-Language", "en"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:bad-request"))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_BAD_REQUEST"))
                .andExpect(jsonPath("$.instance").value("/test-validation-body"));
    }

    @Test
    void should_return_bad_request_for_path_variable_type_mismatch() throws Exception {

        mockMvc.perform(get("/test-validation-param/text")
                        .header("Accept-Language", "en"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:bad-request"))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"text\""))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_BAD_REQUEST"))
                .andExpect(jsonPath("$.instance").value("/test-validation-param/text"));
    }


    @Test
    void should_return_bad_request_when_required_request_param_missing() throws Exception {
        mockMvc.perform(get("/test-argument-missing").header("Accept-Language", "en"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:bad-request"))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("test argument missing"))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_BAD_REQUEST"))
                .andExpect(jsonPath("$.instance").value("/test-argument-missing"));
    }

    @Test
    void should_return_internal_error() throws Exception {
        mockMvc.perform(get("/server-error").header("Accept-Language", "en"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:common:internal-error"))
                .andExpect(jsonPath("$.title").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("Unexpected server error."))
                .andExpect(jsonPath("$.errorCode").value("ERR_COMMON_INTERNAL"))
                .andExpect(jsonPath("$.instance").value("/server-error"));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public GlobalExceptionHandlerTest.GlobalController testController() {
            return new GlobalExceptionHandlerTest.GlobalController();
        }
    }

    @RestController
    @Validated
    static class GlobalController {

        @PostMapping("/test-validation-body")
        RequestDTO validationBodyHandler(@Valid @RequestBody final RequestDTO requestDTO) {
            return requestDTO;
        }

        @GetMapping("/test-validation-param/{id}")
        int validationParamHandler(@PathVariable @Max(5) final int id) {
            return id;
        }

        @GetMapping("/test-argument-missing")
        int validationParamHandler() {
            throw new IllegalArgumentException("test argument missing");
        }

        @GetMapping("/server-error")
        void serverError() {
            throw new NullPointerException("test server error");
        }
    }

    record RequestDTO(
            @Size(min = 2, max = 5)
            String name
    ) {
    }
}