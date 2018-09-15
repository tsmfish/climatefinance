import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDetailedSector } from 'app/shared/model/detailed-sector.model';
import { Principal } from 'app/core';
import { DetailedSectorService } from './detailed-sector.service';

@Component({
    selector: 'jhi-detailed-sector',
    templateUrl: './detailed-sector.component.html'
})
export class DetailedSectorComponent implements OnInit, OnDestroy {
    detailedSectors: IDetailedSector[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private detailedSectorService: DetailedSectorService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.detailedSectorService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IDetailedSector[]>) => (this.detailedSectors = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.detailedSectorService.query().subscribe(
            (res: HttpResponse<IDetailedSector[]>) => {
                this.detailedSectors = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDetailedSectors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDetailedSector) {
        return item.id;
    }

    registerChangeInDetailedSectors() {
        this.eventSubscriber = this.eventManager.subscribe('detailedSectorListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
