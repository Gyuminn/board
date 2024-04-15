package org.kb.board.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 이 어노테이션은 FIELD에만 적용가능하다.
@Retention(RetentionPolicy.RUNTIME) // 이 어노테이션은 런타임에도 유지되어야 함을 의미한다. 따라서 리플렉션을 사용하여 런타임에 이 어노테이션 정보에 접근할 수 있다.
// ExcelColumnName이라는 어노테이션을 정의.
// 엑셀 파일의 열(Column) 이름을 지정하기 위해 사용한다.
public @interface ExcelColumnName {
    // name이라는 문자열 타입의 필드를 정의한다.
    // 이 멤버 필드가 생략될 경우 기본값을 빈 문자열로 설정한다.
    String name() default "";
}
