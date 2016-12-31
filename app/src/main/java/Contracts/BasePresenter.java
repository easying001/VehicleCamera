/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;

/**
 * Created by yangjie11 on 2016/9/7.
 */

public interface BasePresenter<V extends BaseView> {
    void attachView(V view);
    void detachView(boolean retainInstance);
}
