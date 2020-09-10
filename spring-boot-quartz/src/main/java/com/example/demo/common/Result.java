package com.example.demo.common;
import java.util.HashMap;
import java.util.Map;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: 响应结果类
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020
 */
public class Result extends HashMap<String, Object> {

    
    public Result() {
		put("code", 200);
	}

	public static Result error() {
		return error(500, "未知异常，请联系管理员");
	}

	public static Result error(String msg) {
		return error(500, msg);
	}

	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Result ok(Object msg) {
		Result r = new Result();
		r.put("msg", msg);
		return r;
	}


	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}

	public static Result ok() {
		return new Result();
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}