package com.amsidh.mvc.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AlbumResponseModel {
    private String albumId;
    private String userId; 
    private String name;
    private String description;
}
