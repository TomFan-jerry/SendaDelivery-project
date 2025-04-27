package com.senda.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 封装分页查询结果
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PageResult<T> {

    private Long total; //总记录数
    private List<T> records; //当前页查询集合
}
