package com.chenshinan.exercises.generic;

/**
 * @author shinan.chen
 * @since 2018/12/3
 */
public class GenericTest<T> {
    private T data;

    public GenericTest(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {
        //测试泛型T
        GenericTest<String> strG = new GenericTest<>(new String("测试"));
        GenericTest<Integer> intG = new GenericTest<>(new Integer(10));
        GenericTest<Number> numG = new GenericTest<>(20);
        GenericTest<Object> objG = new GenericTest<>(new String("Obj"));
        System.out.println(strG.getData());
        System.out.println(intG.getData());
        System.out.println(numG.getData());
        //测试通配符？
//        getExtData(strG,strG.getData().getClass());
        getExtData(intG,intG.getData().getClass());
        getExtData(numG,numG.getData().getClass());
//        getSupData(intG,intG.getData().getClass());
        getSupData(numG,numG.getData().getClass());
        getSupData(objG,objG.getData().getClass());

    }

    /**
     * 泛型的下限
     */
    public static void getExtData(GenericTest<? extends Number> data, Class<?> cls) {
        System.out.println("data :" + data.getData() + "，" + cls.getName());
    }

    /**
     * 泛型的上限
     */
    public static void getSupData(GenericTest<? super Number> data, Class<?> cls) {
        System.out.println("data :" + data.getData() + "，" + cls.getName());
    }
}
