/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorComponent } from 'app/entities/detailed-sector/detailed-sector.component';
import { DetailedSectorService } from 'app/entities/detailed-sector/detailed-sector.service';
import { DetailedSector } from 'app/shared/model/detailed-sector.model';

describe('Component Tests', () => {
    describe('DetailedSector Management Component', () => {
        let comp: DetailedSectorComponent;
        let fixture: ComponentFixture<DetailedSectorComponent>;
        let service: DetailedSectorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorComponent],
                providers: []
            })
                .overrideTemplate(DetailedSectorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DetailedSectorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DetailedSectorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DetailedSector(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.detailedSectors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
