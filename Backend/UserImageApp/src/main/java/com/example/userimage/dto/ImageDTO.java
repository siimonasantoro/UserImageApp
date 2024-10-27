package com.example.userimage.dto;


public class ImageDTO {
    private Long id;
    private String filename;
    private String url;

    public ImageDTO(Long id, String filename, String url) {
        this.id = id;
        this.filename = filename;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setId(Long id) {
		this.id = id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
        return url;
    }
}
