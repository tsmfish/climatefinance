import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Sector } from './sector.model';
import { SectorService } from './sector.service';

@Component({
    selector: 'jhi-sector-detail',
    templateUrl: './sector-detail.component.html'
})
export class SectorDetailComponent implements OnInit, OnDestroy {

    sector: Sector;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sectorService: SectorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSectors();
    }

    load(id) {
        this.sectorService.find(id)
            .subscribe((sectorResponse: HttpResponse<Sector>) => {
                this.sector = sectorResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSectors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sectorListModification',
            (response) => this.load(this.sector.id)
        );
    }
}
