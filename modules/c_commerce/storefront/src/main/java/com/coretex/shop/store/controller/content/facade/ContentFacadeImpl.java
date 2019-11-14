package com.coretex.shop.store.controller.content.facade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.shop.model.content.ContentFile;
import com.coretex.shop.model.content.ContentFolder;
import com.coretex.shop.model.content.ContentImage;
import com.coretex.shop.model.content.ObjectContent;
import com.coretex.shop.model.content.PersistableContentPage;
import com.coretex.shop.model.content.ReadableContentBox;
import com.coretex.shop.model.content.ReadableContentPage;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;
import com.coretex.shop.utils.FilePathUtils;
import com.coretex.shop.utils.ImageFilePath;
import io.searchbox.strings.StringUtils;

@Component("contentFacade")
public class ContentFacadeImpl implements ContentFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentFacade.class);

	public static final String FILE_CONTENT_DELIMETER = "/";

	@Resource
	private ContentService contentService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private FilePathUtils fileUtils;

	@Override
	public ContentFolder getContentFolder(String folder, MerchantStoreItem store) throws Exception {
		try {
			List<String> imageNames = Optional
					.ofNullable(contentService.getContentFilesNames(store.getCode(), FileContentType.IMAGE))
					.orElseThrow(() -> new ResourceNotFoundException("No Folder found for path : " + folder));

			// images from CMS
			List<ContentImage> contentImages = imageNames.stream()
					.map(name -> convertToContentImage(name, store)).collect(Collectors.toList());

			ContentFolder contentFolder = new ContentFolder();
			if (!StringUtils.isBlank(folder)) {
				contentFolder.setPath(URLEncoder.encode(folder, StandardCharsets.UTF_8));
			}
			contentFolder.getContent().addAll(contentImages);
			return contentFolder;

		} catch (ServiceException e) {
			throw new ServiceRuntimeException("Error while getting folder " + e.getMessage(), e);
		}
	}

	private ContentImage convertToContentImage(String name, MerchantStoreItem store) {
		String path = absolutePath(store, null);
		ContentImage contentImage = new ContentImage();
		contentImage.setName(name);
		contentImage.setPath(path);
		return contentImage;
	}

	@Override
	public String absolutePath(MerchantStoreItem store, String file) {
		return new StringBuilder().append(imageUtils.getContextPath())
				.append(imageUtils.buildStaticImageUtils(store, file)).toString();
	}

	@Override
	public void delete(MerchantStoreItem store, String fileName, String fileType) {
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(fileName, "File name cannot be null");
		try {
			FileContentType t = FileContentType.valueOf(fileType);
			contentService.removeFile(store.getCode(), t, fileName);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	@Override
	public List<ReadableContentPage> getContentPage(MerchantStoreItem store, LanguageItem language) {
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(language, "LanguageItem cannot be null");

		try {
			return contentService.listByType(ContentTypeEnum.PAGE, store, language).stream()
					.filter(ContentItem::getVisible)
					.map(content -> convertContentToReadableContentPage(store, language, content))
					.collect(Collectors.toList());
		} catch (ServiceException e) {
			throw new ServiceRuntimeException("Error while getting content " + e.getMessage(), e);
		}
	}

	private ReadableContentPage convertContentToReadableContentPage(MerchantStoreItem store,
																	LanguageItem language, ContentItem content) {
		ReadableContentPage page = new ReadableContentPage();

		page.setName(content.getName());
		page.setPageContent(content.getDescription());
		page.setSlug(content.getSeUrl());
		page.setDisplayedInMenu(content.getLinkToMenu());
		page.setTitle(content.getTitle());
		page.setMetaDetails(content.getMetatagDescription());
		page.setContentType(ContentTypeEnum.PAGE.name());
		page.setCode(content.getCode());
		page.setPath(fileUtils.buildStaticFilePath(store, content.getSeUrl()));
		return page;
	}

	private ContentItem convertContentPageToContent(MerchantStoreItem store, LanguageItem language,
													PersistableContentPage content) {
		ContentItem contentModel = new ContentItem();


		contentModel.setCode(content.getCode());
		contentModel.setContentType(ContentTypeEnum.PAGE);
		contentModel.setMerchantStore(store);
		contentModel.setVisible(true);

		return contentModel;
	}

	private ContentItem convertContentPageToContent(MerchantStoreItem store, LanguageItem language,
													ContentItem content, PersistableContentPage contentPage) {

		content.setCode(content.getCode());
		content.setContentType(com.coretex.enums.commerce_core_model.ContentTypeEnum.PAGE);
		content.setMerchantStore(store);
		content.setVisible(true);

		return content;
	}


	@Override
	public ReadableContentPage getContentPage(String code, MerchantStoreItem store, LanguageItem language) {

		Validate.notNull(code, "ContentItem code cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(language, "LanguageItem cannot be null");

		try {
			ContentItem content = Optional.ofNullable(contentService.getByCode(code, store, language))
					.orElseThrow(() -> new ResourceNotFoundException("No page found : " + code));

			return convertContentToReadableContentPage(store, language, content);

		} catch (ServiceException e) {
			throw new ServiceRuntimeException("Error while getting page " + e.getMessage(), e);
		}
	}

	@Override
	public List<ReadableContentBox> getContentBoxes(ContentTypeEnum type, String codePrefix,
													MerchantStoreItem store, LanguageItem language) {

		Validate.notNull(codePrefix, "content code prefix cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(language, "LanguageItem cannot be null");

		return contentService.getByCodeLike(type, codePrefix, store, language).stream()
				.map(content -> convertContentToReadableContentBox(store, language, content))
				.collect(Collectors.toList());
	}

	@Override
	public void addContentFile(ContentFile file, String merchantStoreCode) {
		try {
			byte[] payload = file.getFile();
			String fileName = file.getName();

			try (InputStream targetStream = new ByteArrayInputStream(payload)) {

				String type = file.getContentType().split(FILE_CONTENT_DELIMETER)[0];
				FileContentType fileType = getFileContentType(type);

				InputContentFile cmsContent = new InputContentFile();
				cmsContent.setFileName(fileName);
				cmsContent.setMimeType(file.getContentType());
				cmsContent.setFile(targetStream);
				cmsContent.setFileContentType(fileType);

				contentService.addContentFile(merchantStoreCode, cmsContent);
			}
		} catch (ServiceException | IOException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	private FileContentType getFileContentType(String type) {
		FileContentType fileType = FileContentType.STATIC_FILE;
		if (type.equals("image")) {
			fileType = FileContentType.IMAGE;
		}
		return fileType;
	}

	private ReadableContentBox convertContentToReadableContentBox(MerchantStoreItem store,
																  LanguageItem language, ContentItem content) {
		ReadableContentBox box = new ReadableContentBox();
		box.setName(content.getName());
		box.setBoxContent(content.getDescription());
		String staticImageFilePath =
				imageUtils.buildStaticImageUtils(store, content.getCode() + ".jpg");
		box.setImage(staticImageFilePath);
		return box;
	}

	@Override
	public ReadableContentBox getContentBox(String code, MerchantStoreItem store, LanguageItem language) {
		Validate.notNull(code, "ContentItem code cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(language, "LanguageItem cannot be null");

		try {
			ContentItem content =
					Optional.ofNullable(contentService.getByCode(code, store, language))
							.orElseThrow(
									() ->
											new ResourceNotFoundException(
													"Resource not found [" + code + "] for store [" + store.getCode() + "]"));

			ReadableContentBox box = new ReadableContentBox();
			if (content != null) {
				box.setName(content.getSeUrl());
				box.setBoxContent(content.getDescription());
			}
			return box;
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	@Override
	public void saveContentPage(PersistableContentPage page, MerchantStoreItem merchantStore,
								LanguageItem language) {
		Validate.notNull(page);
		Validate.notNull(merchantStore);
		Validate.notNull(page.getCode());

		try {
			// check if exists
			ContentItem content = contentService.getByCode(page.getCode(), merchantStore);
			if (content != null) {
				content = convertContentPageToContent(merchantStore, language, content, page);
			} else {
				content = convertContentPageToContent(merchantStore, language, page);
			}
			contentService.saveOrUpdate(content);
		} catch (Exception e) {
			throw new ServiceRuntimeException(e);
		}

	}
}
