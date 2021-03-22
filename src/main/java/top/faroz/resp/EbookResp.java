package top.faroz.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 为实体类封装一个返回信息
 * 这样，可以保证对前端隐藏一些敏感信息
 * 比如id  password等
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EbookResp {
    // private Long id;

    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;
}