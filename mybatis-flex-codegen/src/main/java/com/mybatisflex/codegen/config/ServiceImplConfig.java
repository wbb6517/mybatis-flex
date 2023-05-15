/**
 * Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mybatisflex.codegen.config;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生成 ServiceImpl 的配置。
 *
 * @author 王帅
 * @since 2023-05-15
 */
@Data
@Accessors(chain = true)
public class ServiceImplConfig {

    /**
     * ServiceImpl 类的前缀。
     */
    private String classPrefix = "";

    /**
     * ServiceImpl 类的后缀。
     */
    private String classSuffix = "ServiceImpl";

    /**
     * 自定义 ServiceImpl 的父类。
     */
    private Class<?> supperClass = ServiceImpl.class;

    public String buildSuperClassImport() {
        return supperClass.getName();
    }

    public String buildSuperClassName() {
        return supperClass.getSimpleName();
    }

}