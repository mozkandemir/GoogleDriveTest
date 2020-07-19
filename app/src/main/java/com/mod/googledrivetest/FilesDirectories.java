package com.mod.googledrivetest;

import java.io.Serializable;

public class FilesDirectories implements Serializable {
    public static final long serialVersionUID = 20161120L;

    private long m_Id;
    private final String mName;
    private final String mAncestorsName;
    private final String mExtension;




    public FilesDirectories(long id, String name, String ancestorsName, String extension) {
        this.m_Id = id;
        mName = name;
        mAncestorsName= ancestorsName;
        mExtension = extension;

    }

    public long getId() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public String getAncestorsName() {
        return mAncestorsName;
    }

    public String getExtension() {
        return mExtension;
    }

}
