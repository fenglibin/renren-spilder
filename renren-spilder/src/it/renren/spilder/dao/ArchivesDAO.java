package it.renren.spilder.dao;

import java.util.List;

import it.renren.spilder.dataobject.ArchivesDO;

public interface ArchivesDAO {

    public void insertArchives(ArchivesDO archivesDO);

    public List<ArchivesDO> selectNullDescriptionRecords();

    public int updateDescription(ArchivesDO archivesDO);

    public int updateTitleKeywordsDescription(ArchivesDO archivesDO);
}
