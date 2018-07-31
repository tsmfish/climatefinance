/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClimatefinanceTestModule } from '../../../test.module';
import { SectorComponent } from '../../../../../../main/webapp/app/entities/sector/sector.component';
import { SectorService } from '../../../../../../main/webapp/app/entities/sector/sector.service';
import { Sector } from '../../../../../../main/webapp/app/entities/sector/sector.model';

describe('Component Tests', () => {

    describe('Sector Management Component', () => {
        let comp: SectorComponent;
        let fixture: ComponentFixture<SectorComponent>;
        let service: SectorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [SectorComponent],
                providers: [
                    SectorService
                ]
            })
            .overrideTemplate(SectorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SectorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SectorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Sector(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.sectors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
