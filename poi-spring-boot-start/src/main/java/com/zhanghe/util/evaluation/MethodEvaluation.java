package com.zhanghe.util.evaluation;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.GregorianCalendar;

/**
 * @author: ZhangHe
 * @since: 2020/7/2 8:59
 */
public abstract class MethodEvaluation {
   static ExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration());

    public static Object invoke(Object o,String method,Object value){
           EvaluationContext context = new StandardEvaluationContext ();
           context.setVariable("value",value);
           return   parser.parseExpression(method).getValue(context,o);
    }



}
