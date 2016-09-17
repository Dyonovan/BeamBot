package com.dyonovan.beam.response;

import java.util.List;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/15/2016
 */
public class InteractiveGameListing {
    public int id;
    public int ownerId;
    public String name;
    public String coverUrl;
    public String description;
    public boolean hasPublishedVersions;
    public String updatedAt;
    public String installation;
    public List<InteractiveGameVersions> versions;
}
