import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailedSector } from 'app/shared/model/detailed-sector.model';

@Component({
    selector: 'jhi-detailed-sector-detail',
    templateUrl: './detailed-sector-detail.component.html'
})
export class DetailedSectorDetailComponent implements OnInit {
    detailedSector: IDetailedSector;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ detailedSector }) => {
            this.detailedSector = detailedSector;
        });
    }

    previousState() {
        window.history.back();
    }
}
