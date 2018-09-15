import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISector } from 'app/shared/model/sector.model';

@Component({
    selector: 'jhi-sector-detail',
    templateUrl: './sector-detail.component.html'
})
export class SectorDetailComponent implements OnInit {
    sector: ISector;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sector }) => {
            this.sector = sector;
        });
    }

    previousState() {
        window.history.back();
    }
}
