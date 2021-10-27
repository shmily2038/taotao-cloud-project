package com.taotao.cloud.dfs.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.dfs.api.vo.FileVO;
import com.taotao.cloud.dfs.api.vo.UploadFileVO;
import com.taotao.cloud.dfs.biz.entity.File;
import com.taotao.cloud.dfs.biz.mapper.FileMapper;
import com.taotao.cloud.dfs.biz.service.IFileService;
import com.taotao.cloud.log.annotation.RequestLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理API
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/12 17:42
 */
@Validated
@RestController
@RequestMapping("/file")
@Tag(name = "文件管理API", description = "文件管理API")
public class FileController {

	private final IFileService IFileService;

	public FileController(IFileService IFileService) {
		this.IFileService = IFileService;
	}

	@Operation(summary = "上传单个文件", description = "上传单个文件", method = CommonConstant.POST)
	@RequestLog(description = "上传单个文件")
	@PreAuthorize("hasAuthority('file:upload')")
	@PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
	public Result<UploadFileVO> upload(@RequestPart("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new BusinessException("文件不能为空");
		}
		File upload = IFileService.upload(file);
		//UploadFileVO result = UploadFileVO.builder().id(upload.getId()).url(upload.getUrl())
		//	.build();
		return Result.success(null);
	}

	@Operation(summary = "上传多个文件", description = "上传多个文件", method = CommonConstant.POST)
	@RequestLog(description = "上传多个文件")
	@PreAuthorize("hasAuthority('file:multiple:upload')")
	@PostMapping(value = "/multiple/upload", headers = "content-type=multipart/form-data")
	public Result<List<UploadFileVO>> uploadMultipleFiles(
		@RequestPart("files") MultipartFile[] files) {
		if (files.length == 0) {
			throw new BusinessException("文件不能为空");
		}

		List<File> uploads = Arrays.stream(files)
			.map(IFileService::upload)
			.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(uploads)) {
			//List<UploadFileVO> result = uploads.stream().map(
			//		upload -> UploadFileVO.builder().id(upload.getId()).url(upload.getUrl()).build())
			//	.collect(Collectors.toList());
			return Result.success(null);
		}

		throw new BusinessException("文件上传失败");
	}

	@Operation(summary = "根据id查询文件信息", description = "根据id查询文件信息", method = CommonConstant.GET)
	@RequestLog(description = "根据id查询文件信息")
	@PreAuthorize("hasAuthority('file:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<FileVO> findFileById(@PathVariable(value = "id") Long id) {
		File file = IFileService.findFileById(id);
		FileVO vo = FileMapper.INSTANCE.fileToFileVO(file);
		return Result.success(vo);
	}

	//
	// @ApiOperation(value = "根据文件名删除oss上的文件", notes = "根据文件名删除oss上的文件")
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header", dataType = "String"),
	// 	@ApiImplicitParam(name = "fileName", value = "路径名称", required = true, dataType = "String",
	// 		example = "robot/2019/04/28/1556429167175766.jpg"),
	// })
	// @PostMapping("file/delete")
	// public Result<Object> delete(@RequestParam("fileName") String fileName) {
	// 	return fileUploadService.delete(fileName);
	// }
	//
	// @ApiOperation(value = "查询oss上的所有文件", notes = "查询oss上的所有文件")
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header", dataType = "String"),
	// })
	// @GetMapping("file/list")
	// public Result<List<OSSObjectSummary>> list() {
	// 	return fileUploadService.list();
	// }
	//
	// @ApiOperation(value = "根据文件名下载oss上的文件", notes = "根据文件名下载oss上的文件")
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header", dataType = "String"),
	// 	@ApiImplicitParam(name = "fileName", value = "路径名称", required = true, dataType = "String",
	// 		example = "robot/2019/04/28/1556429167175766.jpg"),
	// })
	// @GetMapping("file/download")
	// public void download(@RequestParam("fileName") String objectName, HttpServletResponse response) throws IOException {
	// 	//通知浏览器以附件形式下载
	// 	response.setHeader("Content-Disposition",
	// 		"attachment;filename=" + new String(objectName.getBytes(), StandardCharsets.ISO_8859_1));
	// 	fileUploadService.exportOssFile(response.getOutputStream(), objectName);
	// }

}
