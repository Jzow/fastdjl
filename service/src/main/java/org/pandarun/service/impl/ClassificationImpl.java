/*
 * Copyright 2021 Panda Run Organization All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.pandarun.service.impl;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.pandarun.inference.ClassificationInference;

import org.pandarun.service.Classification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ClassName: ClassificationImpl
 * Description: 分类接口实现类
 * Author: James Zow
 * Date: 2020/11/6 9:10
 * Version:
 **/
@Service
public class ClassificationImpl implements Classification {

    Logger log = LoggerFactory.getLogger(ClassificationImpl.class);

    @Override
    public String ImageClassification(String imagePath) {
        String result = "";
        ClassificationInference classificationInference = new ClassificationInference();
        try {
            result = String.valueOf(classificationInference.predict(imagePath));
            log.info(result);
        } catch (IOException e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch (ModelException e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch (TranslateException e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        return result;
    }
}
