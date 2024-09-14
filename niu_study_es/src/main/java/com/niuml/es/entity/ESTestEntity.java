package com.niuml.es.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;

/***
 * @author niumengliang
 * Date:2024/7/25
 * Time:10:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "test")
public class ESTestEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    private String title;

    private String excerpt;
}
