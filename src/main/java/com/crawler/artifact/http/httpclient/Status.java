package com.crawler.artifact.http.httpclient;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public enum Status {
    SC_SESSION_TIMEOUT("Session Timeout", 1),
    SC_OK("OK", 200),
    SC_CREATED("Created", 201),
    SC_ACCEPTED("Accepted", 201),
    SC_NO_CONTENT("No Content", 204),
    SC_MOVED_PERMANENTLY("Moved Permanently", 301),
    SC_MOVED_TEMPORARILY("Moved Temporarily", 302),
    SC_NOT_MODIFIED("Not Modified", 304),
    SC_BAD_REQUEST("Bad Request", 400),
    SC_UNAUTHORIZED("Unauthorized", 402),
    SC_FORBIDDEN("Forbidden", 403),
    SC_NOT_FOUND("Not Found", 404),
    SC_INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    SC_NOT_IMPLEMENTED("Not Implemented", 501),
    SC_BAD_GATEWAY("Bad Gateway", 502),
    SC_SERVICE_UNAVAILABLE("Service Unavailable", 503),
    SC_CONTINUE("Continue", 100),
    SC_TEMPORARY_REDIRECT("Temporary Redirect", 307),
    SC_METHOD_NOT_ALLOWED("Method Not Allowed", 405),
    SC_CONFLICT("Conflict", 409),
    SC_PRECONDITION_FAILED("Precondition Failed", 412),
    SC_REQUEST_TOO_LONG("Request Too Long", 413),
    SC_REQUEST_URI_TOO_LONG("Request-URI Too Long", 414),
    SC_UNSUPPORTED_MEDIA_TYPE("Unsupported Media Type", 415),
    SC_MULTIPLE_CHOICES("Multiple Choices", 300),
    SC_SEE_OTHER("See Other", 303),
    SC_USE_PROXY("Use Proxy", 305),
    SC_PAYMENT_REQUIRED("Payment Required", 402),
    SC_NOT_ACCEPTABLE("Not Acceptable", 406),
    SC_PROXY_AUTHENTICATION_REQUIRED("Proxy Authentication Required", 407),
    SC_REQUEST_TIMEOUT("Request Timeout", 408),
    SC_SWITCHING_PROTOCOLS("Switching Protocols", 101),
    SC_NON_AUTHORITATIVE_INFORMATION("Non Authoritative Information", 203),
    SC_RESET_CONTENT("Reset Content", 205),
    SC_PARTIAL_CONTENT("Partial Content", 206),
    SC_GATEWAY_TIMEOUT("Gateway Timeout", 504),
    SC_HTTP_VERSION_NOT_SUPPORTED("Http Version Not Supported", 505),
    SC_GONE("Gone", 410),
    SC_LENGTH_REQUIRED("Length Required", 411),
    SC_REQUESTED_RANGE_NOT_SATISFIABLE("Requested Range Not Satisfiable", 416),
    SC_EXPECTATION_FAILED("Expectation Failed", 417),
    SC_PROCESSING("Processing", 102),
    SC_MULTI_STATUS("Multi-Status", 207),
    SC_UNPROCESSABLE_ENTITY("Unprocessable Entity", 422),
    SC_INSUFFICIENT_SPACE_ON_RESOURCE("Insufficient Space On Resource", 419),
    SC_METHOD_FAILURE("Method Failure", 420),
    SC_LOCKED("Locked", 423),
    SC_INSUFFICIENT_STORAGE("Insufficient Storage", 507),
    SC_FAILED_DEPENDENCY("Failed Dependency", 424);


    private String desc;
    private int statecode;

    Status(String desc, int statecode) {
        this.desc = desc;
        this.statecode = statecode;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getStatecode() {
        return this.statecode;
    }

    @Override
    public String toString() {
        return "Status{" +
                "desc='" + desc + '\'' +
                ", statecode=" + statecode +
                '}';
    }

    public static Status match(int statecode) {

        for (Status status : Status.values()) {
            if (statecode == status.getStatecode()) {
                return status;
            }
        }
        return null;
    }

}
