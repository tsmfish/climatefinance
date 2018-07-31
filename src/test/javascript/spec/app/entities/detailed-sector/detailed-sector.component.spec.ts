/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorComponent } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector.component';
import { DetailedSectorService } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector.service';
import { DetailedSector } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector.model';

describe('Component Tests', () => {

    describe('DetailedSector Management Component', () => {
        let comp: DetailedSectorComponent;
        let fixture: ComponentFixture<DetailedSectorComponent>;
        let service: DetailedSectorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorComponent],
                providers: [
                    DetailedSectorService
                ]
            })
            .overrideTemplate(DetailedSectorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DetailedSectorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DetailedSectorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DetailedSector(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.detailedSectors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
