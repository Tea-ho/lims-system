package com.lims.lims_study.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.domain.approval.model.ApprovalRequest;
import com.lims.lims_study.domain.approval.model.ApprovalSign;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.product.model.Product;

@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
public class MyBatisConfig {

    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisConfig(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @PostConstruct
    public void registerTypeAliases() {
        TypeAliasRegistry typeAliasRegistry = sqlSessionFactory.getConfiguration().getTypeAliasRegistry();
        
        // 도메인 모델 별칭 등록
        typeAliasRegistry.registerAlias("Approval", Approval.class);
        typeAliasRegistry.registerAlias("ApprovalRequest", ApprovalRequest.class);
        typeAliasRegistry.registerAlias("ApprovalSign", ApprovalSign.class);
        typeAliasRegistry.registerAlias("ApprovalStatus", ApprovalStatus.class);
        typeAliasRegistry.registerAlias("Test", Test.class);
        typeAliasRegistry.registerAlias("TestStage", TestStage.class);
        typeAliasRegistry.registerAlias("User", User.class);
        typeAliasRegistry.registerAlias("Product", Product.class);
    }
}
