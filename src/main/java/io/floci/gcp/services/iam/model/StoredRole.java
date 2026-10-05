package io.floci.gcp.services.iam.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

/** Custom role (iam.googleapis.com v1 {@code Role}); {@code deleted} and empty fields are omitted from the wire form. */
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StoredRole {

    private String name;
    private String title;
    private String description;
    private List<String> includedPermissions = new ArrayList<>();
    private String stage = "GA";
    private String etag;
    private Boolean deleted;

    public StoredRole() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getIncludedPermissions() { return includedPermissions; }
    public void setIncludedPermissions(List<String> includedPermissions) { this.includedPermissions = includedPermissions; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getEtag() { return etag; }
    public void setEtag(String etag) { this.etag = etag; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
}
