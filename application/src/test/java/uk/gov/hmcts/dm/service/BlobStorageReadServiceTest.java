package uk.gov.hmcts.dm.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.gov.hmcts.dm.componenttests.TestUtil;
import uk.gov.hmcts.dm.domain.DocumentContentVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BlobContainerClient.class, BlockBlobClient.class})
@PowerMockIgnore({"javax.net.ssl.*"})
public class BlobStorageReadServiceTest {

    private BlobStorageReadService blobStorageReadService;
    private BlockBlobClient blob;
    private BlobContainerClient cloudBlobContainer;
    private BlobClient blobClient;
    private DocumentContentVersion documentContentVersion;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        cloudBlobContainer = PowerMockito.mock(BlobContainerClient.class);
        blobClient = PowerMockito.mock(BlobClient.class);
        blob = PowerMockito.mock(BlockBlobClient.class);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        outputStream = mock(OutputStream.class);

        when(cloudBlobContainer.getBlobClient(any())).thenReturn(blobClient);
        when(blobClient.getBlockBlobClient()).thenReturn(blob);

        documentContentVersion = TestUtil.DOCUMENT_CONTENT_VERSION;
        blobStorageReadService = new BlobStorageReadService(cloudBlobContainer);
    }

    @Test
    public void loadsBlob() throws IOException {
        blobStorageReadService.loadBlob(documentContentVersion, request, response);

        verify(blob).download(OutputStream.nullOutputStream());
    }

    @Test
    public void doesBinaryExist() {
        given(blob.exists()).willReturn(true);
        Assert.assertTrue(blobStorageReadService.doesBinaryExist(documentContentVersion.getId()));
    }

}
