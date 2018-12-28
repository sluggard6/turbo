package xyz.sluggard.transmatch.vo;

public class HttpResult<T> {
	private static final int DEFAULT_SUCCESS_CODE = 200;
	
	private static final String DEFAULT_SUCCESS_MSG = "success";
	
	private static final int DEFAULT_FAILED_CODE = 500;
	
	private static final String DEFAULT_FAILED_MSG = "failed";
	
	private int code;
	
	private String msg;
	
	private T data;
	
	private static void checkApplication() {
//		if(getApplication() == null) throw new NullPointerException("system can not be null");
	}
	
	private void checkCode() {
		if(code > 999 || code < 0) throw new IllegalArgumentException("code between 0 to 9999 : " + code);
	}
	
	public static <T> HttpResult<T> newResult(int code, String msg, T data) {
		checkApplication();
		return new HttpResult<T>(code, msg, data);
	}
	
	public static <T> HttpResult<T> newResult(int code, String msg) {
		checkApplication();
		return new HttpResult<T>(code, msg, null);
	}
	
	public static <T> HttpResult<T> newResult(int code) {
		checkApplication();
		return new HttpResult<T>(code, "", null);
	}
	
	public static <T> HttpResult<T> SUCCESS(T data) {
		checkApplication();
		return new HttpResult<T>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
	}
	
	public static <T> HttpResult<T> SUCCESS() {
		checkApplication();
		return new HttpResult<T>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, null);
	}
	
	public static <T> HttpResult<T> FAILED(T data) {
		checkApplication();
		return new HttpResult<T>(DEFAULT_FAILED_CODE, DEFAULT_FAILED_MSG, data);
	}
	
	public static <T> HttpResult<T> FAILED() {
		checkApplication();
		return new HttpResult<T>(DEFAULT_FAILED_CODE, DEFAULT_FAILED_MSG, null);
	}

	public HttpResult(int code, String msg, T data) {
		super();
		checkCode();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode() {
//		if(code == DEFAULT_SUCCESS_CODE)return code;
//		else return Application.getCurrentApplication().getErrorCodeId() + code;
		return code;
	}

	public void setCode(int code) {
		checkApplication();
		checkCode();
		this.code = code;
	}

//	public static Application getApplication() {
//		return Application.getCurrentApplication();
//	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HttpResult [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}
