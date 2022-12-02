package sejong.transport.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class BeanConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean // JPAQueryFactory를 빈으로 등록 -> Querydsl을 DI 받아 사용하기 위해
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
