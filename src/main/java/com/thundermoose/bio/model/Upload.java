package com.thundermoose.bio.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Upload {
	private List<Item>	files = new ArrayList<Item>();

	public List<Item> getFiles() {
		return files;
	}

	public void setFiles(List<Item> files) {
		this.files = files;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Item {
		private String	name;
		private long	size;
		private String	url;
		private String	thumbnail_url;
		private String	delete_url;
		private String	delete_type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getThumbnail_url() {
			return thumbnail_url;
		}

		public void setThumbnail_url(String thumbnail_url) {
			this.thumbnail_url = thumbnail_url;
		}

		public String getDelete_url() {
			return delete_url;
		}

		public void setDelete_url(String delete_url) {
			this.delete_url = delete_url;
		}

		public String getDelete_type() {
			return delete_type;
		}

		public void setDelete_type(String delete_type) {
			this.delete_type = delete_type;
		}

	}
}
