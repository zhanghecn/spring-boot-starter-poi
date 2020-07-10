package com.zhanghe.poi.test;

import com.zhanghe.poi.util.evaluation.MethodEvaluation;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author: ZhangHe
 * @since: 2020/7/7 11:23
 */
public class MethodExpressionParserTest {
    @Test
    public void test(){
        Inventor inventor = new Inventor();
        MethodEvaluation.invoke(inventor,"setName(#value)",111);
        MethodEvaluation.invoke(inventor,"setName(#value)","");
        MethodEvaluation.invoke(inventor,"setName(#value)",3.0);
        System.out.println(inventor);
    }

    static class Inventor {

        private String name;
        private String nationality;
        private String[] inventions;

        public Inventor() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setName(Integer name) {
            this.name = name+"";
        }

        public void setName(Double name) {
            this.name = name+"";
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }


        public void setInventions(String[] inventions) {
            this.inventions = inventions;
        }

        public String[] getInventions() {
            return inventions;
        }

        @Override
        public String toString() {
            return "Inventor{" +
                    "name='" + name + '\'' +
                    ", nationality='" + nationality + '\'' +
                    ", inventions=" + Arrays.toString(inventions) +
                    '}';
        }
    }
}
