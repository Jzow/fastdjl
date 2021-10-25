/*
 * Copyright 2021 Apache All Rights Reserved.
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
package org.pandarun.inference;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ClassName: AnimalRecognition
 * Description: 动物分类推理类
 * Author: James Zow
 * Date: 2020/10/23 17:24
 * Version: 1.0
 **/
public class ClassificationInference {

    private final Logger logger = LoggerFactory.getLogger(ClassificationInference.class);

    public Classifications predict(String imagePath) throws IOException, ModelException, TranslateException {
        // 全局变量没给null
        Image image = null;
        if (imagePath.contains("http") && imagePath.endsWith(".jpg") || imagePath.endsWith(".png")
                || imagePath.endsWith(".jpeg") || imagePath.endsWith(".gif")){
            image = ImageFactory.getInstance().fromUrl(imagePath);
        }else {
            Path path = Paths.get(imagePath);
            image = ImageFactory.getInstance().fromFile(path);
        }
        String modelUrl = "https://alpha-djl-demos.s3.amazonaws.com/model/djl-blockrunner/mxnet_resnet18.zip?model_name=resnet18_v1";

        Translator<Image, Classifications> translator =
                ImageClassificationTranslator.builder()
                        .setPipeline(new Pipeline().add(new Resize(224, 224)).add(new ToTensor()))
                        .optApplySoftmax(true)
                        .build();

        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                        .setTypes(Image.class, Classifications.class)
                        .optTranslator(translator)
                        .optModelUrls("mxnet/model/mxnet_resnet18")
                        .optModelName("resnet18_v1")
                        .build();

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
                Classifications animalObjects = predictor.predict(image);
                return animalObjects;
            }
        }
    }

}
