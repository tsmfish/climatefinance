import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MethodologyService } from './';

const initialMarkdown = require('text-loader!./methodology.component.md');
const initialTitle = 'Methodology & Assumptions';

@Component({
    selector: 'jhi-methodology',
    templateUrl: 'methodology.component.html'
})
export class MethodologyComponent implements OnInit {
    private markdown = initialMarkdown;
    private title = initialTitle;

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
