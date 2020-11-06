package com.zhch.paysvc.core.session;


import com.zhch.paysvc.core.config.Constants;

/**
 * @author lumos
 */
public class AnonymousUserSubject extends UserSubject {

    private static final long serialVersionUID = -2047441793201037844L;

    public AnonymousUserSubject() {
        this.setUserId(Constants.ANONYMOUS_USER_ID);
    }
}
