package cn.luischen.interceptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: zk
 * @date: 2024-05-25 14:12
 */
public class Pattern {
    private static Set<String> patterns = new HashSet<String>();
    private static Pattern instance = new Pattern();

    private Pattern() {
    }

    ;

    public static Pattern getInstance() {
        return instance;
    }

    public Pattern add(String path) {
        patterns.add(path);
        return this;
    }


    public Pattern defaultPattern() {
        return getInstance()
                .add("/admin/login")
                .add("/admin/css")
                .add("/admin/images")
                .add("/admin/js")
                .add("/admin/plugins")
                .add("/admin/editormd")
                .add("/admin/attach/presignedUrl");
    }

    public List<String> toList() {
        return new ArrayList(patterns);
    }

}
