package com.chenshinan.exercises.idea;import java.util.ArrayList;import java.util.List;import java.util.Optional;/** * @author shinan.chen * @since 2018/12/5 */public class ShortCutKeyTest {    public static void main(String[] args) {        List<String> list = new ArrayList<>();        String test = "as";        list.add(test);        Boolean bl = true;        /**         * live template 快捷键         */        //main//        public static void main(String[] args){////        }        //logger//        public static final Logger LOGGER = LoggerFactory.getLogger(ShortCutKeyTest.class);        /**         * postfix 快捷键         */        //.nn        if (test != null) {        }        //.null        if (test == null) {        }        //.opt        Optional.of(test);        //.try//        try {//            test//        } catch (Exception e) {//            e.printStackTrace();//        }        //.if        if (bl) {        }    }}