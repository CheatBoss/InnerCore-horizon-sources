package com.zhekasmirnov.innercore.modpack.installation;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.util.*;
import java.util.zip.*;
import java.io.*;

public class ExternalZipFileInstallationSource extends ZipFileInstallationSource implements Closeable
{
    public ExternalZipFileInstallationSource(final InputStream inputStream) throws IOException {
        final File file = new File(FileTools.DIR_WORK, "temp/modpack_tmp");
        file.getParentFile().mkdirs();
        file.delete();
        FileUtils.unpackInputStream(inputStream, file);
        this.setFile(new ZipFile(file));
    }
    
    @Override
    public void close() throws IOException {
    }
}
