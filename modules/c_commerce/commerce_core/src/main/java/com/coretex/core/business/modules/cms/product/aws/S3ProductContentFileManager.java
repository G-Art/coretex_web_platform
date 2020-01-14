package com.coretex.core.business.modules.cms.product.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.modules.cms.impl.CMSManager;
import com.coretex.core.business.modules.cms.product.ProductImageGet;
import com.coretex.core.business.modules.cms.product.ProductImagePut;
import com.coretex.core.business.modules.cms.product.ProductImageRemove;
import com.coretex.core.model.catalog.product.file.ProductImageSize;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.core.model.content.OutputContentFile;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductItem content file manager with AWS S3
 *
 * @author carlsamson
 */
public class S3ProductContentFileManager
		implements ProductImagePut, ProductImageGet, ProductImageRemove {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3ProductContentFileManager.class);


	private static S3ProductContentFileManager fileManager = null;

	private static String DEFAULT_BUCKET_NAME = "shopizer";
	private static String DEFAULT_REGION_NAME = "us-east-1";
	private static final String ROOT_NAME = "products";

	private static final char UNIX_SEPARATOR = '/';
	private static final char WINDOWS_SEPARATOR = '\\';


	private final static String SMALL = "SMALL";
	private final static String LARGE = "LARGE";

	private CMSManager cmsManager;

	public static S3ProductContentFileManager getInstance() {

		if (fileManager == null) {
			fileManager = new S3ProductContentFileManager();
		}

		return fileManager;

	}

	@Override
	public List<OutputContentFile> getImages(String merchantStoreCode,
											 FileContentType imageContentType)  {
		try {
			// get buckets
			String bucketName = bucketName();


			ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
					.withBucketName(bucketName).withPrefix(nodePath(merchantStoreCode));

			List<OutputContentFile> files = null;
			final AmazonS3 s3 = s3Client();
			ListObjectsV2Result results = s3.listObjectsV2(listObjectsRequest);
			List<S3ObjectSummary> objects = results.getObjectSummaries();
			for (S3ObjectSummary os : objects) {
				if (files == null) {
					files = new ArrayList<OutputContentFile>();
				}
				String mimetype = URLConnection.guessContentTypeFromName(os.getKey());
				if (!StringUtils.isBlank(mimetype)) {
					S3Object o = s3.getObject(bucketName, os.getKey());
					byte[] byteArray = IOUtils.toByteArray(o.getObjectContent());
					ByteArrayOutputStream baos = new ByteArrayOutputStream(byteArray.length);
					baos.write(byteArray, 0, byteArray.length);
					OutputContentFile ct = new OutputContentFile();
					ct.setFile(baos);
					files.add(ct);
				}
			}


			LOGGER.info("ProductItem getImages");
			return files;
		} catch (final Exception e) {
			LOGGER.error("Error while getting files", e);
			throw new RuntimeException(e);

		}
	}

	@Override
	public void removeImages(String merchantStoreCode)  {
		try {
			// get buckets
			String bucketName = bucketName();

			final AmazonS3 s3 = s3Client();
			s3.deleteObject(bucketName, nodePath(merchantStoreCode));

			LOGGER.info("Remove folder");
		} catch (final Exception e) {
			LOGGER.error("Error while removing folder", e);
			throw new RuntimeException(e);

		}

	}

	@Override
	public void removeProductImage(ProductImageItem productImage) {
		try {
			// get buckets
			String bucketName = bucketName();

			final AmazonS3 s3 = s3Client();
			s3.deleteObject(bucketName, nodePath(productImage.getProduct().getStore().getCode(),
					productImage.getProduct().getCode()) + productImage.getProductImage());

			LOGGER.info("Remove file");
		} catch (final Exception e) {
			LOGGER.error("Error while removing file", e);
		}

	}

	@Override
	public void removeProductImages(ProductItem product) {
		try {
			// get buckets
			String bucketName = bucketName();

			final AmazonS3 s3 = s3Client();
			s3.deleteObject(bucketName, nodePath(product.getStore().getCode(), product.getCode()));

			LOGGER.info("Remove file");
		} catch (final Exception e) {
			LOGGER.error("Error while removing file", e);
		}

	}

	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName, ProductImageSize size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputContentFile getProductImage(ProductImageItem productImage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OutputContentFile> getImages(ProductItem product) {
		return null;
	}

	@Override
	public void addProductImage(ProductImageItem productImage, ImageContentFile contentImage)
			 {


		try {
			// get buckets
			String bucketName = bucketName();
			final AmazonS3 s3 = s3Client();

			String nodePath = this.nodePath(productImage.getProduct().getStore().getCode(),
					productImage.getProduct().getCode(), contentImage);


			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(contentImage.getMimeType());

			PutObjectRequest request = new PutObjectRequest(bucketName,
					nodePath + productImage.getProductImage(), contentImage.getFile(), metadata);
			request.setCannedAcl(CannedAccessControlList.PublicRead);


			s3.putObject(request);


			LOGGER.info("ProductItem add file");

		} catch (final Exception e) {
			LOGGER.error("Error while removing file", e);
			throw new RuntimeException(e);

		}


	}


	private Bucket getBucket(String bucket_name) {
		final AmazonS3 s3 = s3Client();
		Bucket named_bucket = null;
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket b : buckets) {
			if (b.getName().equals(bucket_name)) {
				named_bucket = b;
			}
		}

		if (named_bucket == null) {
			named_bucket = createBucket(bucket_name);
		}

		return named_bucket;
	}

	private Bucket createBucket(String bucket_name) {
		final AmazonS3 s3 = s3Client();
		Bucket b = null;
		if (s3.doesBucketExistV2(bucket_name)) {
			System.out.format("Bucket %s already exists.\n", bucket_name);
			b = getBucket(bucket_name);
		} else {
			try {
				b = s3.createBucket(bucket_name);
			} catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage());
			}
		}
		return b;
	}

	/**
	 * Builds an amazon S3 client
	 *
	 * @return
	 */
	private AmazonS3 s3Client() {

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(regionName()) // The first region to
				// try your request
				// against
				.build();

		return s3;
	}

	private String bucketName() {
		String bucketName = getCmsManager().getRootName();
		if (StringUtils.isBlank(bucketName)) {
			bucketName = DEFAULT_BUCKET_NAME;
		}
		return bucketName;
	}

	private String regionName() {
		String regionName = getCmsManager().getLocation();
		if (StringUtils.isBlank(regionName)) {
			regionName = DEFAULT_REGION_NAME;
		}
		return regionName;
	}

	private String nodePath(String store) {
		return new StringBuilder().append(ROOT_NAME).append(Constants.SLASH).append(store)
				.append(Constants.SLASH).toString();
	}

	private String nodePath(String store, String product) {

		StringBuilder sb = new StringBuilder();
		// node path
		String nodePath = nodePath(store);
		sb.append(nodePath);

		// product path
		sb.append(product).append(Constants.SLASH);
		return sb.toString();

	}

	private String nodePath(String store, String product, ImageContentFile contentImage) {

		StringBuilder sb = new StringBuilder();
		// node path
		String nodePath = nodePath(store, product);
		sb.append(nodePath);

		// small large
		if (contentImage.getFileContentType().name().equals(FileContentType.PRODUCT.name())) {
			sb.append(SMALL);
		} else if (contentImage.getFileContentType().name().equals(FileContentType.PRODUCTLG.name())) {
			sb.append(LARGE);
		}

		return sb.append(Constants.SLASH).toString();


	}

	public static String getName(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}


	public CMSManager getCmsManager() {
		return cmsManager;
	}

	public void setCmsManager(CMSManager cmsManager) {
		this.cmsManager = cmsManager;
	}


}
