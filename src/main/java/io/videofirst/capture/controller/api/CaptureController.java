/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-present, Video First Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.videofirst.capture.controller.api;

import io.videofirst.capture.model.capture.Capture;
import io.videofirst.capture.model.capture.CaptureRecordParams;
import io.videofirst.capture.model.capture.CaptureStatus;
import io.videofirst.capture.model.capture.CaptureStopParams;
import io.videofirst.capture.model.capture.CaptureSummary;
import io.videofirst.capture.model.capture.UploadStatus;
import io.videofirst.capture.service.CaptureService;
import io.videofirst.capture.service.UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller which is used to capture video / test data, then  upload when finished.
 *
 * @author Bob Marks
 */
@RestController
@RequestMapping("/captures")
@RequiredArgsConstructor
public class CaptureController {

    private final CaptureService captureService;
    private final UploadService uploadService;

    @GetMapping
    public List<CaptureSummary> list() {
        List<CaptureSummary> list = captureService.list();
        return list;
    }

    @GetMapping("/{captureId}")
    public Capture select(@PathVariable final String captureId) {
        Capture capture = captureService.select(captureId);
        return capture;
    }

    @PostMapping("/record")
    public CaptureStatus record(
        @RequestBody(required = false) CaptureRecordParams captureRecordParams) {
        if (captureRecordParams == null) {
            captureRecordParams = CaptureRecordParams.builder().build();
        }
        CaptureStatus status = captureService.record(captureRecordParams);
        return status;
    }

    @PostMapping("/stop")
    public CaptureStatus stop(
        @RequestBody(required = false) CaptureStopParams captureStopParams) {
        if (captureStopParams == null) {
            captureStopParams = CaptureStopParams.builder().build();
        }
        CaptureStatus status = captureService.stop(captureStopParams);
        return status;
    }

    @PostMapping("/cancel")
    public CaptureStatus cancel() {
        CaptureStatus status = captureService.cancel();
        return status;
    }

    @GetMapping("/status")
    public CaptureStatus status() {
        CaptureStatus status = captureService.status();
        return status;
    }

    @DeleteMapping("/{captureId}")
    public ResponseEntity<Void> delete(@PathVariable final String captureId) {
        captureService.delete(captureId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/{captureId}")
    public List<UploadStatus> uploadByCaptureId(@PathVariable final String captureId) {
        uploadService.upload(captureId);
        return uploadStatus();
    }

    @GetMapping("/upload")
    public List<UploadStatus> uploadStatus() {
        return uploadService.status();
    }

}
