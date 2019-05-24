package net.cakecdn.agent.filenode.dto;


public class AjaxResult {

    private static final String OK = "ok";
    private static final String FAILED = "failed";

    private Meta meta;
    private Object data;

    private AjaxResult() {
    }

    public static AjaxResult success() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.meta = new Meta(true, OK);
        return ajaxResult;
    }

    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.meta = new Meta(true, OK);
        ajaxResult.data = data;
        return ajaxResult;
    }

    public static AjaxResult failure() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.meta = new Meta(false, FAILED);
        return ajaxResult;
    }

    public static AjaxResult failure(String message) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.meta = new Meta(false, message);
        return ajaxResult;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }

    public static class Meta {

        private boolean success;
        private String message;

        public Meta(boolean success) {
            this.success = success;
        }

        Meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}