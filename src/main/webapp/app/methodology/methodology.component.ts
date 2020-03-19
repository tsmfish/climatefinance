import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MethodologyService } from './';
import { PdfExportService } from '../shared';

import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import Alignment from '@ckeditor/ckeditor5-alignment/src/alignment';

const initialMarkdown = require('text-loader!./methodology.component.md');
const initialTitle = 'Methodology & Assumptions';

@Component({
    templateUrl: 'methodology.component.html',
    styleUrls: ['./methodology.css']
})
export class MethodologyComponent implements OnInit {
    markdown = initialMarkdown;
    title = initialTitle;

    constructor(private methodologyService: MethodologyService, private pdfService: PdfExportService) {}

    ngOnInit() {
        this.methodologyService.get().subscribe(
            resp => {
                this.markdown = resp.markdown;
                this.title = resp.title;
            },
            err => console.warn('Cannot fetch content for methodology: %o', err)
        );
    }
    exportPdf(element: HTMLElement) {
        this.pdfService.exportPdf(element, 'html');
    }
}

@Component({
    templateUrl: 'methodology-edit.component.html'
})
export class MethodologyEditComponent implements OnInit {
    Editor = ClassicEditor;
    markdown = initialMarkdown;
    title = initialTitle;

    showSaveCompleted = false;
    saveInProgress = false;

    config = {
        toolbar: [
            'heading',
            '|',
            'bold',
            'italic',
            'link',
            'bulletedList',
            'numberedList',
            '|',
            'imageUpload',
            'blockQuote',
            'insertTable',
            'mediaEmbed',
            'undo',
            'redo'
        ]
    };
    constructor(private methodologyService: MethodologyService) {}

    ngOnInit() {
        this.methodologyService.get().subscribe(
            resp => {
                this.markdown = resp.markdown;
                this.title = resp.title;
            },
            err => console.warn('Cannot fetch content for methodology: %o', err)
        );
    }
    saveChanges() {
        this.showSaveCompleted = false;
        this.saveInProgress = true;
        this.methodologyService
            .save({
                title: this.title,
                markdown: this.markdown
            })
            .subscribe(
                () => {
                    this.showSaveCompleted = true;
                    this.saveInProgress = false;
                },
                err => console.warn('Cannot save methodology: %o', err)
            );
    }
}
