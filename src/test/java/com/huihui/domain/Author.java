package com.huihui.domain;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class Author {
    private int id;
    private String username;
    private String password;
    private Blog blog;

    public Author() {
    }

    public Author(String password, String username,int bi) {
        this.blog=new Blog(bi,"b+i",this);
        this.password = password;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
