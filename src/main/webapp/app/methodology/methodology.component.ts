import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MethodologyService } from './';

const initialMarkdown = require('text-loader!./methodology.component.md');
const initialTitle = 'Methodology & Assumptions';

@Component({
    templateUrl: 'methodology.component.html'
})
export class MethodologyComponent implements OnInit {
    markdown = initialMarkdown;
    title = initialTitle;

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
}

@Component({
    templateUrl: 'methodology-edit.component.html'
})
export class MethodologyEditComponent implements OnInit {
    markdown = initialMarkdown;
    title = initialTitle;

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
        this.methodologyService
            .save({
                title: this.title,
                markdown: this.markdown
            })
            .subscribe(() => {}, err => console.warn('Cannot save methodology: %o', err));
    }
}
