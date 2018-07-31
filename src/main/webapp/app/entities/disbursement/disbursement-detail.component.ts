import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDisbursement } from 'app/shared/model/disbursement.model';

@Component({
    selector: 'jhi-disbursement-detail',
    templateUrl: './disbursement-detail.component.html'
})
export class DisbursementDetailComponent implements OnInit {
    disbursement: IDisbursement;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ disbursement }) => {
            this.disbursement = disbursement;
        });
    }

    previousState() {
        window.history.back();
    }
}
