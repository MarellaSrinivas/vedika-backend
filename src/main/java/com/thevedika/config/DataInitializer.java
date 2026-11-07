package com.thevedika.config;

import com.thevedika.entity.Amenity;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAddon;
import com.thevedika.entity.FarmhouseAmenity;
import com.thevedika.entity.FarmhouseAvailability;
import com.thevedika.entity.FarmhouseImage;
import com.thevedika.entity.FarmhouseSpecialPrice;

import com.thevedika.repository.AmenityRepository;
import com.thevedika.repository.FarmhouseAddonRepository;
import com.thevedika.repository.FarmhouseAmenityRepository;
import com.thevedika.repository.FarmhouseAvailabilityRepository;
import com.thevedika.repository.FarmhouseImageRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.FarmhouseSpecialPriceRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeTheVedikaData(
            FarmhouseRepository farmhouseRepository,
            FarmhouseImageRepository imageRepository,
            AmenityRepository amenityRepository,
            FarmhouseAmenityRepository farmhouseAmenityRepository,
            FarmhouseAddonRepository addonRepository,
            FarmhouseAvailabilityRepository availabilityRepository,
            FarmhouseSpecialPriceRepository specialPriceRepository) {

        return args -> {

            /*
             * Do not insert duplicate seed data.
             *
             * If farmhouses already exist, this initializer does nothing.
             */
            if (farmhouseRepository.count() > 0) {
                System.out.println(
                        "TheVedika seed data already exists. Skipping initialization.");
                return;
            }

            System.out.println("Creating TheVedika development data...");

            /*
             * ============================================================
             * AMENITIES
             * ============================================================
             */

            Amenity privatePool = getOrCreateAmenity(
                    amenityRepository,
                    "Private Pool");

            Amenity acRooms = getOrCreateAmenity(
                    amenityRepository,
                    "AC Rooms");

            Amenity wifi = getOrCreateAmenity(
                    amenityRepository,
                    "WiFi");

            Amenity parking = getOrCreateAmenity(
                    amenityRepository,
                    "Parking");

            Amenity kitchen = getOrCreateAmenity(
                    amenityRepository,
                    "Kitchen");

            Amenity bbq = getOrCreateAmenity(
                    amenityRepository,
                    "BBQ");

            Amenity bonfire = getOrCreateAmenity(
                    amenityRepository,
                    "Bonfire");

            Amenity indoorGames = getOrCreateAmenity(
                    amenityRepository,
                    "Indoor Games");

            Amenity largeLawn = getOrCreateAmenity(
                    amenityRepository,
                    "Large Lawn");

            Amenity musicSystem = getOrCreateAmenity(
                    amenityRepository,
                    "Music System");

            Amenity garden = getOrCreateAmenity(
                    amenityRepository,
                    "Garden");

            Amenity outdoorGames = getOrCreateAmenity(
                    amenityRepository,
                    "Outdoor Games");

            Amenity firePit = getOrCreateAmenity(
                    amenityRepository,
                    "Fire Pit");

            /*
             * ============================================================
             * FARMHOUSE 1
             * ============================================================
             */

            Farmhouse farmhouse1 = createFarmhouse(
                    farmhouseRepository,

                    "The Vedika Garden Villa",
                    "Moinabad, Hyderabad",

                    "A beautiful private farmhouse surrounded by greenery, "
                            + "perfect for family gatherings, birthdays and "
                            + "weekend getaways.",

                    "12000",
                    "16000",
                    "500",

                    5,
                    15,
                    4,
                    4,

                    4.9,
                    124,

                    "2:00 PM",
                    "11:00 AM");

            addImages(
                    imageRepository,
                    farmhouse1,
                    List.of(
                            "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?auto=format&fit=crop&w=1200&q=80"));

            addAmenities(
                    farmhouseAmenityRepository,
                    farmhouse1,
                    List.of(
                            privatePool,
                            acRooms,
                            wifi,
                            parking,
                            kitchen,
                            bbq,
                            bonfire,
                            indoorGames));

            addAddons(
                    addonRepository,
                    farmhouse1,

                    addon(
                            "Gas Cooking Access",
                            "Gas stove and basic cooking access",
                            "500",
                            "perStay"),

                    addon(
                            "Food Package",
                            "Food arranged per guest; menu confirmed with host",
                            "350",
                            "perGuest"),

                    addon(
                            "Barbecue Setup",
                            "Barbecue equipment and setup",
                            "1500",
                            "perStay"));

            addBlockedDates(
                    availabilityRepository,
                    farmhouse1,
                    List.of(
                            LocalDate.of(2026, 11, 2),
                            LocalDate.of(2026, 11, 8),
                            LocalDate.of(2026, 11, 9),
                            LocalDate.of(2026, 11, 15),
                            LocalDate.of(2026, 11, 22)));

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse1,
                    LocalDate.of(2026, 11, 6),
                    "18000");

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse1,
                    LocalDate.of(2026, 11, 7),
                    "18000");

            /*
             * ============================================================
             * FARMHOUSE 2
             * ============================================================
             */

            Farmhouse farmhouse2 = createFarmhouse(
                    farmhouseRepository,

                    "Green Valley Luxury Farmhouse",
                    "Shamshabad, Hyderabad",

                    "A spacious luxury farmhouse with a private swimming "
                            + "pool, large lawn and entertainment facilities.",

                    "15000",
                    "19000",
                    "600",

                    5,
                    20,
                    5,
                    5,

                    4.8,
                    86,

                    "1:00 PM",
                    "11:00 AM");

            addImages(
                    imageRepository,
                    farmhouse2,
                    List.of(
                            "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?auto=format&fit=crop&w=1200&q=80"));

            addAmenities(
                    farmhouseAmenityRepository,
                    farmhouse2,
                    List.of(
                            privatePool,
                            largeLawn,
                            acRooms,
                            parking,
                            bbq,
                            musicSystem,
                            outdoorGames));

            addAddons(
                    addonRepository,
                    farmhouse2,

                    addon(
                            "Food Package",
                            "Food package charged per guest",
                            "400",
                            "perGuest"),

                    addon(
                            "Barbecue Setup",
                            "Professional barbecue setup",
                            "1800",
                            "perStay"),

                    addon(
                            "Music System",
                            "Premium outdoor music system",
                            "1200",
                            "perStay"));

            addBlockedDates(
                    availabilityRepository,
                    farmhouse2,
                    List.of(
                            LocalDate.of(2026, 11, 4),
                            LocalDate.of(2026, 11, 5),
                            LocalDate.of(2026, 11, 12),
                            LocalDate.of(2026, 11, 18),
                            LocalDate.of(2026, 11, 19),
                            LocalDate.of(2026, 11, 28)));

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse2,
                    LocalDate.of(2026, 11, 14),
                    "22000");

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse2,
                    LocalDate.of(2026, 11, 15),
                    "22000");

            /*
             * ============================================================
             * FARMHOUSE 3
             * ============================================================
             */

            Farmhouse farmhouse3 = createFarmhouse(
                    farmhouseRepository,

                    "Palm Breeze Farm Stay",
                    "Chevella, Hyderabad",

                    "A peaceful farm stay surrounded by trees and greenery, "
                            + "ideal for families and small groups.",

                    "8500",
                    "11000",
                    "400",

                    5,
                    10,
                    3,
                    3,

                    4.7,
                    62,

                    "2:00 PM",
                    "11:00 AM");

            addImages(
                    imageRepository,
                    farmhouse3,
                    List.of(
                            "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600607688969-a5bfcd646154?auto=format&fit=crop&w=1200&q=80"));

            addAmenities(
                    farmhouseAmenityRepository,
                    farmhouse3,
                    List.of(
                            privatePool,
                            garden,
                            acRooms,
                            parking,
                            wifi,
                            bonfire,
                            firePit));

            addAddons(
                    addonRepository,
                    farmhouse3,

                    addon(
                            "Gas Cooking Access",
                            "Basic gas stove and cooking area",
                            "400",
                            "perStay"),

                    addon(
                            "Food Package",
                            "Home-style food package per guest",
                            "300",
                            "perGuest"),

                    addon(
                            "Bonfire Setup",
                            "Firewood and bonfire setup",
                            "1000",
                            "perStay"));

            addBlockedDates(
                    availabilityRepository,
                    farmhouse3,
                    List.of(
                            LocalDate.of(2026, 11, 6),
                            LocalDate.of(2026, 11, 7),
                            LocalDate.of(2026, 11, 13),
                            LocalDate.of(2026, 11, 21),
                            LocalDate.of(2026, 11, 22)));

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse3,
                    LocalDate.of(2026, 11, 20),
                    "13000");

            /*
             * ============================================================
             * FARMHOUSE 4
             * ============================================================
             */

            Farmhouse farmhouse4 = createFarmhouse(
                    farmhouseRepository,

                    "Lakeview Serenity Retreat",
                    "Shankarpally, Hyderabad",

                    "A premium countryside retreat with beautiful views, "
                            + "large outdoor spaces and peaceful surroundings.",

                    "13500",
                    "17500",
                    "500",

                    6,
                    16,
                    4,
                    4,

                    4.9,
                    91,

                    "1:30 PM",
                    "11:00 AM");

            addImages(
                    imageRepository,
                    farmhouse4,
                    List.of(
                            "https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600607688960-e095ff83135c?auto=format&fit=crop&w=1200&q=80"));

            addAmenities(
                    farmhouseAmenityRepository,
                    farmhouse4,
                    List.of(
                            privatePool,
                            largeLawn,
                            garden,
                            acRooms,
                            wifi,
                            parking,
                            outdoorGames,
                            firePit));

            addAddons(
                    addonRepository,
                    farmhouse4,

                    addon(
                            "Food Package",
                            "Premium food package per guest",
                            "450",
                            "perGuest"),

                    addon(
                            "Barbecue Setup",
                            "Barbecue grill and setup",
                            "1600",
                            "perStay"),

                    addon(
                            "Event Setup",
                            "Basic birthday/event decoration setup",
                            "3500",
                            "perStay"));

            addBlockedDates(
                    availabilityRepository,
                    farmhouse4,
                    List.of(
                            LocalDate.of(2026, 11, 1),
                            LocalDate.of(2026, 11, 10),
                            LocalDate.of(2026, 11, 11),
                            LocalDate.of(2026, 11, 17),
                            LocalDate.of(2026, 11, 25),
                            LocalDate.of(2026, 11, 26)));

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse4,
                    LocalDate.of(2026, 11, 27),
                    "21000");

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse4,
                    LocalDate.of(2026, 11, 28),
                    "21000");

            /*
             * ============================================================
             * FARMHOUSE 5
             * ============================================================
             */

            Farmhouse farmhouse5 = createFarmhouse(
                    farmhouseRepository,

                    "Sunset Grove Private Estate",
                    "Vikarabad Road, Hyderabad",

                    "A spacious private estate designed for larger groups, "
                            + "celebrations and relaxed weekend stays.",

                    "18000",
                    "23000",
                    "700",

                    8,
                    25,
                    6,
                    6,

                    4.8,
                    73,

                    "2:00 PM",
                    "12:00 PM");

            addImages(
                    imageRepository,
                    farmhouse5,
                    List.of(
                            "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?auto=format&fit=crop&w=1200&q=80",
                            "https://images.unsplash.com/photo-1600585154363-67eb9e2e2099?auto=format&fit=crop&w=1200&q=80"));

            addAmenities(
                    farmhouseAmenityRepository,
                    farmhouse5,
                    List.of(
                            privatePool,
                            largeLawn,
                            acRooms,
                            parking,
                            kitchen,
                            bbq,
                            bonfire,
                            musicSystem,
                            indoorGames,
                            outdoorGames));

            addAddons(
                    addonRepository,
                    farmhouse5,

                    addon(
                            "Food Package",
                            "Full food package per guest",
                            "500",
                            "perGuest"),

                    addon(
                            "Barbecue Setup",
                            "Large barbecue setup for groups",
                            "2000",
                            "perStay"),

                    addon(
                            "Event Decoration",
                            "Basic event and birthday decoration",
                            "5000",
                            "perStay"),

                    addon(
                            "Extra Mattress",
                            "Extra sleeping mattress",
                            "500",
                            "perStay"));

            addBlockedDates(
                    availabilityRepository,
                    farmhouse5,
                    List.of(
                            LocalDate.of(2026, 11, 3),
                            LocalDate.of(2026, 11, 4),
                            LocalDate.of(2026, 11, 5),
                            LocalDate.of(2026, 11, 16),
                            LocalDate.of(2026, 11, 23),
                            LocalDate.of(2026, 11, 24),
                            LocalDate.of(2026, 11, 30)));

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse5,
                    LocalDate.of(2026, 11, 14),
                    "26000");

            addSpecialPrice(
                    specialPriceRepository,
                    farmhouse5,
                    LocalDate.of(2026, 11, 15),
                    "26000");

            System.out.println(
                    "==================================================");

            System.out.println(
                    "TheVedika development data created successfully.");

            System.out.println(
                    "Farmhouses: 5");

            System.out.println(
                    "Amenities: 12");

            System.out.println(
                    "Images, add-ons, blocked dates and special prices created.");

            System.out.println(
                    "==================================================");
        };
    }

    /*
     * ================================================================
     * FARMHOUSE CREATION
     * ================================================================
     */

    private Farmhouse createFarmhouse(
            FarmhouseRepository repository,

            String name,
            String location,
            String description,

            String basePrice,
            String weekendPrice,
            String extraGuestPrice,

            int minimumGuests,
            int maximumGuests,
            int bedrooms,
            int bathrooms,

            double rating,
            int reviewCount,

            String checkInTime,
            String checkOutTime) {

        Farmhouse farmhouse = new Farmhouse();

        farmhouse.setName(name);
        farmhouse.setLocation(location);
        farmhouse.setDescription(description);

        farmhouse.setBasePrice(
                new BigDecimal(basePrice));

        farmhouse.setWeekendPrice(
                new BigDecimal(weekendPrice));

        farmhouse.setExtraGuestPrice(
                new BigDecimal(extraGuestPrice));

        farmhouse.setMinimumGuests(minimumGuests);
        farmhouse.setMaximumGuests(maximumGuests);

        farmhouse.setBedrooms(bedrooms);
        farmhouse.setBathrooms(bathrooms);

        /*
         * If your Farmhouse.rating field is BigDecimal,
         * change this line to:
         *
         * farmhouse.setRating(new BigDecimal(String.valueOf(rating)));
         */
        farmhouse.setRating(
                new BigDecimal(String.valueOf(rating)));
        farmhouse.setReviewCount(reviewCount);

        farmhouse.setCheckInTime(checkInTime);
        farmhouse.setCheckOutTime(checkOutTime);

        farmhouse.setActive(true);

        return repository.save(farmhouse);
    }

    /*
     * ================================================================
     * AMENITIES
     * ================================================================
     */

    private Amenity getOrCreateAmenity(
            AmenityRepository repository,
            String name) {

        return repository.findByNameIgnoreCase(name)
                .orElseGet(() -> {

                    Amenity amenity = new Amenity();

                    amenity.setName(name);

                    return repository.save(amenity);
                });
    }

    private void addAmenities(
            FarmhouseAmenityRepository repository,
            Farmhouse farmhouse,
            List<Amenity> amenities) {

        for (Amenity amenity : amenities) {

            FarmhouseAmenity farmhouseAmenity = new FarmhouseAmenity();

            farmhouseAmenity.setFarmhouse(farmhouse);
            farmhouseAmenity.setAmenity(amenity);

            repository.save(farmhouseAmenity);
        }
    }

    /*
     * ================================================================
     * IMAGES
     * ================================================================
     */

    private void addImages(
            FarmhouseImageRepository repository,
            Farmhouse farmhouse,
            List<String> imageUrls) {

        int displayOrder = 1;

        for (String imageUrl : imageUrls) {

            FarmhouseImage image = new FarmhouseImage();

            image.setFarmhouse(farmhouse);
            image.setImageUrl(imageUrl);
            image.setDisplayOrder(displayOrder++);

            repository.save(image);
        }
    }

    /*
     * ================================================================
     * ADD-ONS
     * ================================================================
     */

    private FarmhouseAddon addon(
            String name,
            String description,
            String price,
            String pricingType) {

        FarmhouseAddon addon = new FarmhouseAddon();

        addon.setName(name);
        addon.setDescription(description);
        addon.setPrice(new BigDecimal(price));
        addon.setPricingType(pricingType);
        addon.setAvailable(true);

        return addon;
    }

    private void addAddons(
            FarmhouseAddonRepository repository,
            Farmhouse farmhouse,
            FarmhouseAddon... addons) {

        for (FarmhouseAddon addon : addons) {

            addon.setFarmhouse(farmhouse);

            repository.save(addon);
        }
    }

    /*
     * ================================================================
     * BLOCKED / UNAVAILABLE DATES
     * ================================================================
     */

    private void addBlockedDates(
            FarmhouseAvailabilityRepository repository,
            Farmhouse farmhouse,
            List<LocalDate> dates) {

        for (LocalDate date : dates) {

            FarmhouseAvailability availability = new FarmhouseAvailability();

            availability.setFarmhouse(farmhouse);
            availability.setAvailabilityDate(date);
            availability.setAvailable(false);

            repository.save(availability);
        }
    }

    /*
     * ================================================================
     * SPECIAL PRICES
     * ================================================================
     */

    private void addSpecialPrice(
            FarmhouseSpecialPriceRepository repository,
            Farmhouse farmhouse,
            LocalDate date,
            String price) {

        FarmhouseSpecialPrice specialPrice = new FarmhouseSpecialPrice();

        specialPrice.setFarmhouse(farmhouse);
        specialPrice.setPriceDate(date);
        specialPrice.setPrice(new BigDecimal(price));

        repository.save(specialPrice);
    }
}