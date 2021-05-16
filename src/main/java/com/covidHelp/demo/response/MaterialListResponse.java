package com.covidHelp.demo.response;

import java.util.ArrayList;
import java.util.List;

import com.covidHelp.demo.model.Material;
import com.covidHelp.demo.model.MaterialType;

public class MaterialListResponse {
    private String district;
    private MaterialType materialType;
    private String category;
    private List<Material> data;


    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public MaterialType getMaterialType() {
        return this.materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Material> getData() {
        return this.data == null ? new ArrayList<Material>() : this.data;
    }

    public void setData(List<Material> data) {
        this.data = data;
    }

}
