package com.pmavio.pmbaseframe.interfaces;

import com.pmavio.pmbaseframe.bean.BaseBean;

public interface SaveDb<T extends BaseBean> {
    void saveDb(T t);

    void saveDb(Iterable<T> objects);
}
