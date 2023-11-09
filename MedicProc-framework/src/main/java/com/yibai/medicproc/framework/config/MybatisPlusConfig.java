package com.yibai.medicproc.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.yibai.medicproc.common.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 *
 * @author 冷澳
 * @date 2023/11/8
 */
//@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }



    /**
     * 自定义主键生成策略
     * */
    @Bean
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
        return plusProperties -> plusProperties.getGlobalConfig().setIdentifierGenerator(new CustomIdGenerator());
    }

    @Slf4j
    @Component
    public static class CustomMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            //版本号默认值
            this.strictInsertFill(metaObject, "createTime",  () -> 1, Integer.class);
            /*创建时间*/
//            this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
//            /*注册时间*/
//            this.strictInsertFill(metaObject, "registrationTime", LocalDateTime::now, LocalDateTime.class);
//            /*最后登录时间*/
//            this.strictUpdateFill(metaObject, "lastLoginTime", LocalDateTime::now, LocalDateTime.class);
//            /*上传时间*/
//            this.strictInsertFill(metaObject, "uploadTime", LocalDateTime::now, LocalDateTime.class);
//            /*更新时间*/
//            this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        }

        @Override
        public void updateFill(MetaObject metaObject) {
//            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
//            this.strictUpdateFill(metaObject, "lastLoginTime", LocalDateTime::now, LocalDateTime.class);
        }
    }


    private static class CustomIdGenerator implements IdentifierGenerator {
        @Override
        public Long nextId(Object entity) {
            //返回生成的id值即可.
            return SnowflakeIdWorker.generateId();
        }
    }
}
